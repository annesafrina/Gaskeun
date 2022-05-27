package com.mpp.gaskeun.dto;

import com.mpp.gaskeun.model.Car;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Setter @Getter
@Slf4j
public class CarDto {
    public static final int MAX_SIZE = 65_535;
    String licensePlate;
    int capacity;
    String transmission;
    long priceRate;
    String color;
    String model;
    String availableStart;
    String availableEnd;
    String base64image;
    String description;
    String cityName;

    public void fillDto(Car car) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        licensePlate = car.getLicensePlate();
        capacity = car.getCapacity();
        transmission = car.getTransmissionName();
        priceRate = car.getPriceRate();
        color = car.getColorName();
        model = car.getModel();
        availableStart = formatter.format(car.getAvailableStartDate());
        availableEnd = formatter.format(car.getAvailableEndDate());
        base64image = car.getPicture();
        description = car.getDescription();
        cityName = car.getLocationName();
    }

    public void setImage(MultipartFile imageFile) throws IOException {
        if(imageIsValid(imageFile)) {
            byte[] image = Base64.encodeBase64(imageFile.getBytes());
            this.base64image = new String(image);

        } else if(base64image == null) {
            throw new IOException("File size is above 64 KB or it does not exist.");
        }
    }

    private boolean imageIsValid(MultipartFile imageFile) {
        return imageFile != null && !imageFile.isEmpty() && imageFile.getSize() <= MAX_SIZE;
    }

    public boolean isComplete() {
        return !(licensePlate == null || licensePlate.isBlank()) &&
                capacity > 0 &&
                !(transmission == null || transmission.isBlank()) &&
                priceRate > 0 &&
                !(color == null || color.isBlank()) &&
                !(model == null || model.isBlank()) &&
                !(availableStart == null || availableStart.isBlank()) &&
                !(availableEnd == null || availableEnd.isBlank()) &&
                !(base64image == null || base64image.isBlank()) &&
                !(description == null || description.isBlank()) &&
                !(cityName == null || cityName.isBlank());
    }
}
