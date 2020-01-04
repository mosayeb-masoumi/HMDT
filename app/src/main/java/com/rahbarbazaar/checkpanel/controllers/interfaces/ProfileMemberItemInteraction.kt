package com.rahbarbazaar.checkpanel.controllers.interfaces

import com.rahbarbazaar.checkpanel.models.profile.MemberDetail


interface ProfileMemberItemInteraction {
   fun profileMemberitemOnClicked(member_detail: MemberDetail, position: Int)
}