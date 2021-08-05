/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class LinkDto {
  @XmlElement private Long id;
  @XmlElement private Long typeId;
  @XmlElement private Long src;
  @XmlElement private Long dst;
  @XmlElement private String name;
  @XmlElement private IssueDto issue;
}
