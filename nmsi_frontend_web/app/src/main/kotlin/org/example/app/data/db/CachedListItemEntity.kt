package org.example.app.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Stores list rows for a given section kind and auth scope.
 *
 * The cache is keyed by (userId, role, kind) so users/roles do not share cached lists.
 */
@Entity(
    tableName = "cached_list_items",
    indices = [
        Index(value = ["userId", "role", "kind"]),
        Index(value = ["userId", "role", "kind", "itemId"], unique = true),
    ],
)
data class CachedListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0L,

    /** Signed-in user id (or "anon"). */
    val userId: String,

    /** Primary app role string (ADMIN/STAFF/VOLUNTEER or "NONE"). */
    val role: String,

    /** Section kind string (DONATIONS/VOLUNTEERS/PROJECTS/DONOR_CRM/REPORTS/SETTINGS). */
    val kind: String,

    /** Stable id for the domain item (donation id / volunteer id / etc). */
    val itemId: String,

    /** Text used for UI row title. */
    val title: String,

    /** Text used for UI row subtitle. */
    val subtitle: String,

    /** For deterministic display ordering. */
    val sortOrder: Int,
)
