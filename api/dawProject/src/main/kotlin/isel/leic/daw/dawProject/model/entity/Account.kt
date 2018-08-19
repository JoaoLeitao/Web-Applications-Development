package isel.leic.daw.dawProject.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
data class Account(

        @JsonIgnore
        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        var checklists: MutableSet<Checklist>? = HashSet(),

        @JsonIgnore
        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        var templates: MutableSet<Template>? = HashSet(),

        @Id val sub: String = ""
) : Serializable{
        override fun equals(other: Any?): Boolean {
                if(this === other) return true
                return if (other !is Account) false else sub == other.sub
        }

        override fun hashCode(): Int {
                return 100
        }
}