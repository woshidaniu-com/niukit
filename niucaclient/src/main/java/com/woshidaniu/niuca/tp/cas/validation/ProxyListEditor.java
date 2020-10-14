package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public final class ProxyListEditor extends PropertyEditorSupport {
    public ProxyListEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        ArrayList proxyChains = new ArrayList();

        try {
            String line;
            try {
                while((line = reader.readLine()) != null) {
                    if (CommonUtils.isNotBlank(line)) {
                        proxyChains.add(line.trim().split(" "));
                    }
                }
            } catch (IOException var13) {
            }
        } finally {
            try {
                reader.close();
            } catch (IOException var12) {
            }

        }

        this.setValue(new ProxyList(proxyChains));
    }
}
