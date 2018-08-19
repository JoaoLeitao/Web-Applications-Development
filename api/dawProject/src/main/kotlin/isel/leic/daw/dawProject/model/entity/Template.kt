package isel.leic.daw.dawProject.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class Template(
        @NotNull
        @Size(max = 128)
        var templateName: String = "",

        @JsonIgnore
        @OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
        var checklists: MutableSet<Checklist>? = HashSet(),

        @JsonIgnore
        @OneToMany(mappedBy = "template", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        val templateItems: MutableSet<TemplateItem>? = HashSet(),

        @JsonIgnore
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username")
        var account: Account? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val templateId: Int = -1
) : Serializable{
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        return if (other !is Template) false else templateId == other.templateId
    }

    override fun hashCode(): Int {
        return 7
    }
}