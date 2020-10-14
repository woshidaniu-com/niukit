package com.woshidaniu.niuca.tp.cas.validation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public final class Cas10TicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {
    public Cas10TicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected String getUrlSuffix() {
        return "validate";
    }

    protected Assertion parseResponseFromServer(String response) throws TicketValidationException {
        if (!response.startsWith("yes")) {
            throw new TicketValidationException("CAS Server could not validate ticket.");
        } else {
            try {
                BufferedReader reader = new BufferedReader(new StringReader(response));
                reader.readLine();
                String name = reader.readLine();
                return new AssertionImpl(name);
            } catch (IOException var4) {
                throw new TicketValidationException("Unable to parse response.", var4);
            }
        }
    }
}
