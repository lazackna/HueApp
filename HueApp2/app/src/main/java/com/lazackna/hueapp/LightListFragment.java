package com.lazackna.hueapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lazackna.hueapp.Light.ColorLight;
import com.lazackna.hueapp.Light.DimmableLight;
import com.lazackna.hueapp.Light.Light;

import java.util.ArrayList;
import java.util.List;

public class LightListFragment extends Fragment implements LightAdapter.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public List<Light> lights;
    private RecyclerView recipesList;
    private LightAdapter recipesAdapter;

    private OnFragmentInteractionListener listener;

    @Override
    public void onItemClick(int clickedPosition) {
        Log.d("LightListFragment", "onItemClick() called, item " + clickedPosition + " clicked");
        if (listener != null) {
            listener.onFragmentInteraction(lights.get(clickedPosition));
        }
    }

    public LightListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LightListFragment", "onCreate() called");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("RecipeListFragment", "onCreateView() called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lightlist, container, false);
        lights = (ArrayList<Light>) requireArguments().get("lights");; //TODO light een waarde geven
        //lights.add(new DimmableLight(1, "id", "name", 20, Light.PowerState.OFF));
//        for (Light r : lights) {
//            Log.i("RecipeListFragment", r.toString());
//        }

        recipesList = view.findViewById(R.id.fragment_recyclerView);
        recipesAdapter = new LightAdapter(lights, this);
        recipesList.setAdapter(recipesAdapter);
        recipesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recipesList.addItemDecoration(itemDecoration);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("RecipeListFragment", "onAttach() called");
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Light light);
    }
}
