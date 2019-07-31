package com.slickqa.jupiter.parameterized;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {
    public static String generateFor(Object[] arguments) {
        String checksum = "err";
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            boolean atLeastOneArgumentSerialized = false;
            for(Object argument : arguments) {
                ByteArrayOutputStream serializedArguments = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream serializer = new ObjectOutputStream(serializedArguments);
                    serializer.writeObject(argument);
                    hasher.update(serializedArguments.toByteArray());
                    atLeastOneArgumentSerialized = true;
                } catch (IOException e) {

                }
            }
            if(atLeastOneArgumentSerialized) {
                checksum = DatatypeConverter.printHexBinary(hasher.digest());
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return checksum;
    }
}
