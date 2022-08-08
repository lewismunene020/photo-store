package com.example.photostore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UserAccountAdapter extends FragmentStateAdapter {


    public UserAccountAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){return  new  Sign_Up_Fragment();   }
        else{ return  new Sign_In_Fragment();    }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
