package isel.leic.daw.dawProject.service

import isel.leic.daw.dawProject.configuration.NotFoundException
import isel.leic.daw.dawProject.model.inputModel.TemplateItemDTO
import isel.leic.daw.dawProject.model.entity.Template
import isel.leic.daw.dawProject.model.entity.TemplateItem
import isel.leic.daw.dawProject.repository.TemplateItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TemplateItemService {

    @Autowired
    lateinit var templateItemRepository: TemplateItemRepository

    fun deleteById(id : Int){
        templateItemRepository.deleteById(id)
    }

    fun save(t: Template, ti: TemplateItemDTO) : TemplateItem {
        return templateItemRepository.saveAndFlush(
                TemplateItem(
                        templateItemName = ti.templateItemName,
                        templateItemId = ti.templateItemId,
                        description = ti.description,
                        template = t
                )
        )
    }

    fun getUserTemplateItem(template: Template, templateItemId: Int): TemplateItem {
        return templateItemRepository
                .findByTemplateItemIdAndTemplate(templateItemId, template)
                .orElseThrow{ NotFoundException("Template Item ID $templateItemId not found") }
    }
}