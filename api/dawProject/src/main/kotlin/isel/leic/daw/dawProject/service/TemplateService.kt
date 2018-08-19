package isel.leic.daw.dawProject.service

import isel.leic.daw.dawProject.configuration.ForbiddenException
import isel.leic.daw.dawProject.configuration.NotFoundException
import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.inputModel.TemplateDTO
import isel.leic.daw.dawProject.model.entity.Template
import isel.leic.daw.dawProject.repository.TemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.ServletRequest

@Service
class TemplateService {

    @Autowired
    lateinit var templateRepository: TemplateRepository

    @Autowired
    lateinit var checklistService: ChecklistService

    fun deleteById(id : Int, user: ServletRequest){
        val template = templateRepository
                .findById(id)
                .orElseThrow { NotFoundException("Template ID $id not found") }

        if(user.getAttribute("sub") != template.account!!.sub) throw ForbiddenException(
                "Template ID $id is forbidden to the current user " + user.getAttribute("username"))

        val checklists = checklistService.checklistRepository.findByTemplate(template)

        checklists.forEach { it.template = null }
        checklistService.checklistRepository.saveAll(checklists)

        templateRepository.deleteById(id)
    }

    fun save(dto: TemplateDTO, user: ServletRequest) :  Template{
        return templateRepository.saveAndFlush(
                Template(
                        templateName = dto.templateName,
                        templateId = dto.templateId,
                        templateItems = dto.templateItems,
                        checklists = dto.checklists,
                        account = Account(sub = user.getAttribute("sub") as String)
                )
        )
    }

    fun getUserTemplate(id: Int, user: ServletRequest) : Template {
        val t: Template = templateRepository
                .findById(id)
                .orElseThrow { NotFoundException("Template ID $id not found") }
        return if(t.account!!.sub == user.getAttribute("sub")) t else throw ForbiddenException(
                "Template ID $id is forbidden to the current user " + user.getAttribute("username"))
    }

    fun getUserTemplates(user: ServletRequest): List<Template> {
        return templateRepository.findByAccount(Account(sub = user.getAttribute("sub") as String))
    }
}