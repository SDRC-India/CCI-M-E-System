/**
 * 
 */
package org.sdrc.scps.repository;

import org.sdrc.scps.domain.Attachment;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush
 *
 */

@RepositoryDefinition(domainClass=Attachment.class,idClass=Integer.class)
public interface AttachmentRepository {
	
	@Transactional
	Attachment save(Attachment attachment);

	Attachment findByAttachmentId(long fileId);

}
