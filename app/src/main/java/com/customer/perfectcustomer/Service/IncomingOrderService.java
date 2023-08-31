package com.customer.perfectcustomer.Service;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class IncomingOrderService extends Provider.Service
{


    public IncomingOrderService(Provider provider, String type, String algorithm, String className, List<String> aliases, Map<String, String> attributes) {
        super(provider, type, algorithm, className, aliases, attributes);
    }
}
