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

        log.info("IMG {}",base64image);
    }

    public void setImage(MultipartFile imageFile) throws IOException {
        log.info("BEFORE {}", base64image.isBlank());
        if(imageFile != null && !imageFile.isEmpty()) {
            byte[] image = Base64.encodeBase64(imageFile.getBytes());
            this.base64image = new String(image);
        }
        log.info("AFTER {}", base64image.isBlank());
    }

    public boolean isComplete() {
        return !licensePlate.isBlank() &&
                capacity > 0 &&
                !transmission.isBlank() &&
                priceRate > 0 &&
                !color.isBlank() &&
                !model.isBlank() &&
                !availableStart.isBlank() &&
                !availableEnd.isBlank() &&
                !base64image.isBlank() &&
                !description.isBlank() &&
                !cityName.isBlank();
    }
}
