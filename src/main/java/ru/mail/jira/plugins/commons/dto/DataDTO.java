package ru.mail.jira.plugins.commons.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;

@Getter
@Setter
@Builder
@XmlRootElement
public class DataDTO<DTO> {
  @Nullable @XmlElement private DTO data;
  @Nullable @XmlElement private Instant validUntil;

  public static <DTO> DataDTO<DTO> empty() {
    return DataDTO.<DTO>builder().build();
  }

  public static <DTO> DataDTO<DTO> just(DTO data) {
    return DataDTO.<DTO>builder().data(data).build();
  }
}
