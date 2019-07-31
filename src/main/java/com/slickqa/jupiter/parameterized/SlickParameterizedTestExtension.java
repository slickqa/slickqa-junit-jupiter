package com.slickqa.jupiter.parameterized;

import com.slickqa.jupiter.annotations.ParameterizedTest;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumerInitializer;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.junit.platform.commons.util.AnnotationUtils.*;

public class SlickParameterizedTestExtension implements TestTemplateInvocationContextProvider {

    private static final String METHOD_CONTEXT_KEY = "context";

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }

        Method testMethod = context.getTestMethod().get();
        if (!isAnnotated(testMethod, ParameterizedTest.class)) {
            return false;
        }

        ParameterizedTestMethodContext methodContext = new ParameterizedTestMethodContext(testMethod);

        Preconditions.condition(methodContext.hasPotentiallyValidSignature(),
                () -> String.format(
                        "@ParameterizedTest method [%s] declares formal parameters in an invalid order: "
                                + "argument aggregators must be declared after any indexed arguments "
                                + "and before any arguments resolved by another ParameterResolver.",
                        testMethod.toGenericString()));

        getStore(context).put(METHOD_CONTEXT_KEY, methodContext);

        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
            ExtensionContext extensionContext) {

        Method templateMethod = extensionContext.getRequiredTestMethod();
        String displayName = extensionContext.getDisplayName();
        ParameterizedTestMethodContext methodContext = getStore(extensionContext)//
                .get(METHOD_CONTEXT_KEY, ParameterizedTestMethodContext.class);
        ParameterizedTestNameFormatter formatter = createNameFormatter(templateMethod, displayName);
        AtomicLong invocationCount = new AtomicLong(0);
        ArgumentsChecksumStore checksums = new ArgumentsChecksumStore();
        extensionContext.getStore(ExtensionContext.Namespace.create(ArgumentsChecksumStore.class, templateMethod)).put("checksums", checksums);

        if(System.getenv("SLICK_DATA_DRIVEN") != null) {
            return Arrays.asList((TestTemplateInvocationContext)new SlickTemplateInvocationContext(formatter, methodContext)).stream();
        }

        // @formatter:off
        return findRepeatableAnnotations(templateMethod, ArgumentsSource.class)
                .stream()
                .map(ArgumentsSource::value)
                .map(this::instantiateArgumentsProvider)
                .map(provider -> AnnotationConsumerInitializer.initialize(templateMethod, provider))
                .flatMap(provider -> arguments(provider, extensionContext))
                .map(Arguments::get)
                .map(arguments -> checksumArguments(arguments, invocationCount.get(), checksums))
                .map(arguments -> consumedArguments(arguments, methodContext))
                .map(arguments -> createInvocationContext(formatter, methodContext, arguments))
                .peek(invocationContext -> invocationCount.incrementAndGet())
                .onClose(() ->
                        Preconditions.condition(invocationCount.get() > 0,
                                "Configuration error: You must configure at least one set of arguments for this @ParameterizedTest"));
        // @formatter:on
    }

    @SuppressWarnings("ConstantConditions")
    private ArgumentsProvider instantiateArgumentsProvider(Class<? extends ArgumentsProvider> clazz) {
        try {
            return ReflectionUtils.newInstance(clazz);
        }
        catch (Exception ex) {
            if (ex instanceof NoSuchMethodException) {
                String message = String.format("Failed to find a no-argument constructor for ArgumentsProvider [%s]. "
                                + "Please ensure that a no-argument constructor exists and "
                                + "that the class is either a top-level class or a static nested class",
                        clazz.getName());
                throw new JUnitException(message, ex);
            }
            throw ex;
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(SlickParameterizedTestExtension.class, context.getRequiredTestMethod()));
    }

    private TestTemplateInvocationContext createInvocationContext(ParameterizedTestNameFormatter formatter,
                                                                  ParameterizedTestMethodContext methodContext, Object[] arguments) {
        return new ParameterizedTestInvocationContext(formatter, methodContext, arguments);
    }

    private ParameterizedTestNameFormatter createNameFormatter(Method templateMethod, String displayName) {
        ParameterizedTest parameterizedTest = findAnnotation(templateMethod, ParameterizedTest.class).get();
        String pattern = Preconditions.notBlank(parameterizedTest.name().trim(),
                () -> String.format(
                        "Configuration error: @ParameterizedTest on method [%s] must be declared with a non-empty name.",
                        templateMethod));
        return new ParameterizedTestNameFormatter(pattern, displayName);
    }

    protected static Stream<? extends Arguments> arguments(ArgumentsProvider provider, ExtensionContext context) {
        try {
            return provider.provideArguments(context);
        }
        catch (Exception e) {
            throw ExceptionUtils.throwAsUncheckedException(e);
        }
    }

    private Object[] checksumArguments(Object[] arguments, long index, ArgumentsChecksumStore store) {
        // compute checksum here
        store.putChecksum(index + 1, Checksum.generateFor(arguments));
        return arguments;
    }

    private Object[] consumedArguments(Object[] arguments, ParameterizedTestMethodContext methodContext) {
        int parameterCount = methodContext.getParameterCount();
        return methodContext.hasAggregator() ? arguments
                : (arguments.length > parameterCount ? Arrays.copyOf(arguments, parameterCount) : arguments);
    }

}
/*
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }

        Method testMethod = context.getTestMethod().get();
        try {
            String message = MessageFormat.format("supportsTestTemplate(method: {0}) called\n", testMethod.getName());
            Files.write(Paths.get("tester.log"), message.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return null;
    }
}

 */
