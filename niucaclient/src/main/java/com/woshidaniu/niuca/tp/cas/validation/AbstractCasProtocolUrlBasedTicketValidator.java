package com.woshidaniu.niuca.tp.cas.validation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractCasProtocolUrlBasedTicketValidator extends AbstractUrlBasedTicketValidator {
    protected AbstractCasProtocolUrlBasedTicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected final String retrieveResponseFromServer(URL validationUrl, String ticket) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection)validationUrl.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer(255);
            synchronized(stringBuffer) {
                String line;
                while((line = in.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }

                String var9 = stringBuffer.toString();
                return var9;
            }
        } catch (IOException var14) {
            this.log.error(var14, var14);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }

        return null;
    }
}
