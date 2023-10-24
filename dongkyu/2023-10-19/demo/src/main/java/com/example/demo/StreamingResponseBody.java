package com.example.demo;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface StreamingResponseBody {

    void writeTo(OutputStream outputStream) throws IOException;
}
