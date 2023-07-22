package bi.seke.deliveryservice.converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, Date> {

    public static final String CURRENT = "${current}";
    private SimpleDateFormat dateFormatter;

    @Value("${config.date-format}")
    public void setDateFormatter(String format) {
        this.dateFormatter = new SimpleDateFormat(format);
    }


    @Override
    public Date convert(final String source) {
        if (StringUtils.equals(source, CURRENT) || StringUtils.isEmpty(source)) {
            return new Date();
        }

        try {
            return dateFormatter.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
