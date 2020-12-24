package com.joesemper.goalmanager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.databinding.DialogEditTitleBinding

class EditTitleDialog(private val parent: TitleChangeDialogListener): DialogFragment(),
    View.OnClickListener {

    private var _binding: DialogEditTitleBinding? = null
    private val binding: DialogEditTitleBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditTitleBinding.inflate(inflater, container, false)
        binding.buttonApply.setOnClickListener(this)
        binding.buttonCancel.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleEt.setText(parent.getTitle())
        binding.descriptionEt.setText(parent.getDescription())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.buttonApply.id) {
            parent.setTitle(binding.titleEt.text.toString())
            parent.setDescription(binding.descriptionEt.text.toString())
        }
        dismiss()
    }
}