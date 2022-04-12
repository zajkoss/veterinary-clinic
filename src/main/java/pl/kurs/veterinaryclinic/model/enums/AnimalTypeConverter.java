package pl.kurs.veterinaryclinic.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AnimalTypeConverter implements AttributeConverter<AnimalType, String> {
    @Override
    public String convertToDatabaseColumn(AnimalType animalType) {
        if(animalType == null)
            return null;
        return animalType.toString();
    }

    @Override
    public AnimalType convertToEntityAttribute(String s) {
        if(s == null)
            return null;
        try {
            return AnimalType.valueOf(s);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}
