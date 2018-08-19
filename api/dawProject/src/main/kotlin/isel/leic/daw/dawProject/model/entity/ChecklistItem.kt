package isel.leic.daw.dawProject.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class ChecklistItem(
        @NotNull @Size(max = 128)
        val checklistItemName: String = "",

        @NotNull @Size(max = 128)
        val state: String = "",

        @NotNull @Size(max = 128)
        val description: String = "",

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_id")
        var checklist: Checklist? = null,

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val checklistItemId: Int = -1
) :  Serializable{
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        return if (other !is ChecklistItem) false else checklistItemId == other.checklistItemId
    }

    override fun hashCode(): Int {
        return 69
    }
}