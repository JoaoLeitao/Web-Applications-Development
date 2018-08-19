package isel.leic.daw.dawProject.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class TemplateItem(
        @NotNull @Size(max = 128)
        val templateItemName: String = "",

        @NotNull @Size(max = 128)
        val description: String = "",

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "template_id")
        var template: Template? = null,

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val templateItemId: Int = -1
) : Serializable{
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        return if (other !is TemplateItem) false else templateItemId == other.templateItemId
    }

    override fun hashCode(): Int {
        return 6
    }
}