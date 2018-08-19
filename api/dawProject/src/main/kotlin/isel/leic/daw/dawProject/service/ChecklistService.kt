package isel.leic.daw.dawProject.service

import isel.leic.daw.dawProject.configuration.ForbiddenException
import isel.leic.daw.dawProject.configuration.NotFoundException
import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.inputModel.ChecklistDTO
import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.repository.ChecklistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.ServletRequest

@Service
class ChecklistService {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository

    @Autowired
    lateinit var templateService: TemplateService

    fun deleteById(id : Int, user: ServletRequest){

        val checklist = checklistRepository
                .findById(id)
                .orElseThrow { NotFoundException("Checklist ID $id not found") }

        if(user.getAttribute("sub") != checklist.account!!.sub) throw ForbiddenException(
                "Checklist ID $id is forbidden to the current user " + user.getAttribute("username"))

        checklistRepository.deleteById(id)
    }

    fun save(dto: ChecklistDTO, user: ServletRequest) : Checklist {
        return checklistRepository.saveAndFlush(
                Checklist(
                        checklistName = dto.checklistName,
                        completionDate = dto.completionDate,
                        checklistId = dto.checklistId,
                        template = if(dto.templateId != -1) templateService.getUserTemplate(dto.templateId, user) else null,
                        checklistItems = dto.checklistItems,
                        account = Account(sub = user.getAttribute("sub") as String)
                )
            )
    }

    fun getUserChecklist(id: Int, user: ServletRequest): Checklist {
        val c: Checklist = checklistRepository
                .findById(id)
                .orElseThrow { NotFoundException("Checklist ID $id not found") }
        return if(c.account!!.sub == user.getAttribute("sub")) c else throw ForbiddenException(
                "Checklist ID $id is forbidden to the current user " + user.getAttribute("username"))
    }

    fun getUserChecklists(user: ServletRequest): List<Checklist> {
        return checklistRepository.findByAccount(Account(sub = user.getAttribute("sub") as String))
    }
}