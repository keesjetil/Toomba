package com.toomba.library.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.toomba.library.models.Book;

public class BookSerializer extends JsonSerializer<Book> {

    @Override
    public void serialize(Book value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(value.getTitle());
    }
}