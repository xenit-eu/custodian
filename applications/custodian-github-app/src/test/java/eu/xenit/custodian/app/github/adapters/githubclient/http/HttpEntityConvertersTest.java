package eu.xenit.custodian.app.github.adapters.githubclient.http;

import static org.assertj.core.api.Assertions.assertThat;

import com.budjb.httprequests.HttpEntity;
import com.budjb.httprequests.exception.UnsupportedConversionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.xenit.custodian.app.github.adapters.githubclient.model.ListInstallationsResponse;
import eu.xenit.custodian.app.github.util.ResourceLoader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.Test;

public class HttpEntityConvertersTest {

    @Test
    public void read() throws IOException, UnsupportedConversionException {
        HttpEntityConverters converters = new HttpEntityConverters(Arrays.asList(
                new JacksonEntityReader(new ObjectMapper())
        ));

        InputStream inputStream = ResourceLoader.getInputStream("classpath:github/app-installations.json");
        HttpEntity httpEntity = new HttpEntity(inputStream);

        ListInstallationsResponse response = converters.read(ListInstallationsResponse.class, httpEntity);
        assertThat(response.get(0).getAccount().getId()).isEqualTo(1L);
    }
}