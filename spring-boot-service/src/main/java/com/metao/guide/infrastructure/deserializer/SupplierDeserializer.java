package com.metao.guide.infrastructure.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.metao.guide.application.Supplier;

import java.io.IOException;
import java.util.Map;

public class SupplierDeserializer extends StdDeserializer<Supplier> {
    private final transient Map<Long, Supplier> supplierMap;

    public SupplierDeserializer(Map<Long, Supplier> supplierMap) {
        super(Supplier.class);
        this.supplierMap = supplierMap;
    }

    @Override
    public Supplier deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        long supplierId = parser.getLongValue();
        Supplier supplier = supplierMap.get(supplierId);
        if (supplier == null) {
            throw new IllegalArgumentException("No supplier found for ID: " + supplierId);
        }
        return supplier;
    }
}
