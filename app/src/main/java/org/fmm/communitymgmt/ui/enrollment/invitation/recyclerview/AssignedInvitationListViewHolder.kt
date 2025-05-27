package org.fmm.communitymgmt.ui.enrollment.invitation.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.fmm.communitymgmt.R
import org.fmm.communitymgmt.databinding.ItemAssignedInvitationBinding
import org.fmm.communitymgmt.domainmodels.model.FullInvitationModel

class AssignedInvitationListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemAssignedInvitationBinding.bind(view)

    //private var idSelected: Int = -1

    fun render(invitation: FullInvitationModel, currentIndex: Int,
               getSelectedIndex: ()->Int,
               setSelectedLambda: ()->Unit,
               onItemSelectedModelLambda: () -> Unit){
        // @TODO Hay que introducir la imagen de la parroquia
        binding.invitationName.text= invitation.name
        binding.community.text = invitation.communityFullName
        binding.city.text = itemView.context.getString(R.string.city_country, invitation
            .communityCity, invitation.communityCountry)
        binding.name.text = invitation.personFullName
        if (getSelectedIndex() == currentIndex)
            binding.photo.setImageResource(R.drawable.ic_check)
        else
            binding.photo.setImageResource(R.drawable.ic_parish)
        binding.photo.setOnClickListener {
            setSelectedLambda()
            flipIcon(binding.photo, getSelectedIndex() == currentIndex)
            onItemSelectedModelLambda()
        }
    }

    // Antes, el setOnClickListener se hacía en el render, pero como queremos esta animación lo tenemos que configurar aquí.
    // Definimos una nueva función lambda: newLambda, sin parámetros, que lo que haga sea llamar a onSignSelected
    private fun startRotationAnimation(view: View, newLambda:()->Unit) {
        val apply = view.animate().apply {
            duration = 500
            interpolator = LinearInterpolator()
            rotationYBy(360f)
            withEndAction { newLambda() }
            start()
        }
    }

    private fun flipIcon(iconView: ImageView, isSelected: Boolean) {
        val firstHalf = ObjectAnimator.ofFloat(iconView, "rotationY", 0f, 90f)
        val secondHalf = ObjectAnimator.ofFloat(iconView, "rotationY", 90f, 180f)

        firstHalf.duration = 150
        secondHalf.duration = 150

        firstHalf.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (isSelected) {
                    iconView.setImageResource(R.drawable.ic_check)
                } else {
                    iconView.setImageResource(R.drawable.ic_parish)
                }
            }
        })
/*
        secondHalf.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                newLambda()
            }
        })

 */
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(firstHalf, secondHalf)
        animatorSet.start()
    }

}