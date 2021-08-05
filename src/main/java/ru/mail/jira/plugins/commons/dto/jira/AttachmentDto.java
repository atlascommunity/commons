/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class AttachmentDto {
  @XmlElement private long id;
  @XmlElement private String fileName;
  @XmlElement private boolean deletable;
  @XmlElement private String filenameUrlEncoded;
  @XmlElement private String signedFilenameUrlEncoded;
  @XmlElement private String authorDisplayName;
  @XmlElement private String createdFormatted;
  @XmlElement private String createdIso8601;
  @XmlElement private String icon;
  @XmlElement private String altText;
  @XmlElement private boolean attachFilePermission;
  @XmlElement private String thumbnailUrl;
  @XmlElement private String previewType;
}
