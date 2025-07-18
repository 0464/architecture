package study.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Dashboard {

    @Id
    private int key;
    private String value;
    private Date date;

}
