package isel.leic.daw.dawProject.service

import isel.leic.daw.dawProject.configuration.NotFoundException
import isel.leic.daw.dawProject.model.inputModel.ChecklistItemDTO
import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.ChecklistItem
import isel.leic.daw.dawProject.repository.ChecklistItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChecklistItemService {

    @Autowired
    lateinit var checklistItemRepository: ChecklistItemRepository

    fun deleteById(id : Int){
        checklistItemRepository.deleteById(id)
    }

    fun save(c: Checklist, ci : ChecklistItemDTO) : ChecklistItem{
        return checklistItemRepository.saveAndFlush(
                ChecklistItem(
                        checklistItemName = ci.checklistItemName,
                        description = ci.description,
                        state = ci.state,
                        checklistItemId = ci.checklistItemId,
                        checklist = c
                )
        )
    }

    fun getUserChecklistItem(checklist: Checklist, checklistItemId: Int): ChecklistItem {
        return checklistItemRepository
                .findByChecklistItemIdAndChecklist(checklistItemId, checklist)
                .orElseThrow{ NotFoundException("Checklist Item ID $checklistItemId not found") }
    }
}