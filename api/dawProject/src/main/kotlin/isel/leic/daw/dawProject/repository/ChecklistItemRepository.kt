package isel.leic.daw.dawProject.repository

import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.ChecklistItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChecklistItemRepository : JpaRepository<ChecklistItem, Int> {
    fun findByChecklistItemIdAndChecklist(checklistItemId: Int, checklist: Checklist): Optional<ChecklistItem>
}