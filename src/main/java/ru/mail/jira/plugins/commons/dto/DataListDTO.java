package ru.mail.jira.plugins.commons.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
@XmlRootElement
public class DataListDTO<DTO> {
  @NotNull @XmlElement private Collection<DTO> data;
  @XmlElement private int total;
  @Nullable @XmlElement private Instant validUntil;

  @NotNull
  public static <DTO> DataListDTO<DTO> empty() {
    return DataListDTO.<DTO>builder().data(Collections.emptyList()).total(0).build();
  }

  @NotNull
  public static <DTO> DataListDTO<DTO> just(@NotNull Collection<DTO> data) {
    return DataListDTO.<DTO>builder().data(data).total(data.size()).build();
  }

  @NotNull
  public static <DTO> DataListDTO<DTO> just(@NotNull Collection<DTO> data, int total) {
    return DataListDTO.<DTO>builder().data(data).total(total).build();
  }
}
