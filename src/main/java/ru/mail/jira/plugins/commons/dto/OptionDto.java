package ru.mail.jira.plugins.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
public class OptionDto {
  @NotNull @XmlElement private String value;
  @NotNull @XmlElement private String label;
  @Nullable @XmlElement private String avatarUrl;
}
