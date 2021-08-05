package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class CustomFieldDto {
  @XmlElement private long id;
  @XmlElement private String name;
  @XmlElement private String value;
  @XmlElement private String html;
}
