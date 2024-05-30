package com.example.akillibesin.frontend.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.akillibesin.R;
import com.example.akillibesin.core.adapters.RecipesAdapter;
import com.example.akillibesin.databinding.FragmentRecipesBinding;


public class RecipesFragment extends Fragment {

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.example.akillibesin.databinding.FragmentRecipesBinding binding = FragmentRecipesBinding.bind(view);

        String[] imageText = {"Makarna", "Karışık Salata", "Yağsız Salata", "\n" +
                "Haşlanmış Yumurta ile Pilav", "Hamburger", "Pankek", "Çikolatalı Kek", "Soslu Makarna"};
        int[] images = {
                R.drawable.recipe_carbonara, R.drawable.recipe_mixed_salad,
                R.drawable.recipe_dry_salad, R.drawable.recipe_fried_rise_with_poached_egg,
                R.drawable.recipe_burger, R.drawable.recipe_pan_cake,
                R.drawable.recipe_chocolate_cake, R.drawable.recipe_pasta_with_white_sause};
        RecipesAdapter recipesAdapter = new RecipesAdapter(getContext(), imageText, images);
        binding.recipesGridview.setAdapter(recipesAdapter);
    }
}