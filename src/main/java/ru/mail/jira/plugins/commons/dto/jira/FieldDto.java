/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Getter
@Setter
public class FieldDto {
  @XmlElement private String id;
  @XmlElement private String name;
  @XmlElement private String value;
  @XmlElement private List<String> values;
  @XmlElement private String html;
}
