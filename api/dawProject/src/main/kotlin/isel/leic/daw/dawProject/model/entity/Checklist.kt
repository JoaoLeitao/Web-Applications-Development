package isel.leic.daw.dawProject.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class Checklist(
        @NotNull @Size(max = 128)
        val checklistName: String = "",

        @NotNull
        val completionDate: LocalDate? = null,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "template_id", nullable = true)
        var template: Template? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "checklist", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklistItems: MutableSet<ChecklistItem>? = HashSet(),

        @JsonIgnore
        @NotNull @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username")
        var account: Account? = null,

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val checklistId: Int = -1
) : Serializable{
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        return if (other !is Checklist) false else checklistId == other.checklistId
    }

    override fun hashCode(): Int {
        return 68
    }
}