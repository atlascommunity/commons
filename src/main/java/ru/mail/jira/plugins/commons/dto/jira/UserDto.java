package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class UserDto {
  @XmlElement private String id;
  @XmlElement private String name;
  @XmlElement private String displayName;
  @XmlElement private String email;
  @XmlElement private String avatarUrl;
  @XmlElement private String profileUrl;
  @XmlElement private String type;
  @XmlElement private String displayNameWithEmail;
}
