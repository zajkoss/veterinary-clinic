package pl.kurs.veterinaryclinic.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DoctorTypeConverter implements AttributeConverter<DoctorType, String> {
    @Override
    public String convertToDatabaseColumn(DoctorType doctorType) {
        if (doctorType == null)
            return null;
        return doctorType.toString();
    }

    @Override
    public DoctorType convertToEntityAttribute(String s) {
        if (s == null)
            return null;
        try {
            return DoctorType.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
