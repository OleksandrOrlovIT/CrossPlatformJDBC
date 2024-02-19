package orlov641p.khai.edu.com.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {
    public static LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public static Date convertLocalDateTimeToSQLDate(LocalDateTime dateToConvert){
        java.util.Date utilDate = java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());

        return new Date(utilDate.getTime());
    }
}
