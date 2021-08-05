/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class IssueTypeDto {
  @XmlElement private String id;
  @XmlElement private String name;
  @XmlElement private String iconUrl;
}
