package com.febrian.startask.ui.parent.account

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.febrian.startask.MainActivity
import com.febrian.startask.databinding.FragmentAccountBinding
import com.febrian.startask.ui.child.ChildHomeActivity
import com.febrian.startask.utils.Constant
import com.febrian.startask.utils.Constant.KEY_NOTIFICATION
import com.febrian.startask.utils.SendNotification

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits", "ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity()
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        val familyId: String? = preferences.getString(Constant.FAMILY_ID, null)
        val name = preferences.getString(Constant.KEY_NAME, "").toString()
        binding.tvTaskFamilyId.text = familyId
        binding.tvTaskName.text = name
        binding.tvTaskRole.text = role

        val check = preferences.getBoolean(KEY_NOTIFICATION, false)
        binding.notificationActive.isChecked = check
        binding.notificationActive.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(KEY_NOTIFICATION, isChecked).apply()
        }

        if(check) {
            val sendNotification = SendNotification()
            sendNotification.setRepeat(requireContext())
        }

        binding.copy.setOnClickListener {
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", familyId)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }

        binding.share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(requireActivity())
                .setType(mimeType)
                .setChooserTitle("Share")
                .setText(familyId)
                .startChooser()
        }


        binding.logout.setOnClickListener {
            preferences.edit().clear().apply()
            val mIntent = Intent(activity, MainActivity::class.java)
            startActivity(mIntent)
            requireActivity().finish()
        }
    }
}