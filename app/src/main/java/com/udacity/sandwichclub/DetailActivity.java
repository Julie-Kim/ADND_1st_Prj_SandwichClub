package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        setImage(sandwich.getImage());
        setAlsoKnownAs(sandwich.getAlsoKnownAs());
        setPlaceOfOrigin(sandwich.getPlaceOfOrigin());
        setDescription(sandwich.getDescription());
        setIngredients(sandwich.getIngredients());
    }

    private void setImage(String imageUrl) {
        ImageView sandwichImageView = findViewById(R.id.image_iv);
        ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(imageUrl)
                .into(sandwichImageView, new ImageLoadedCallback(loadingIndicator) {
                    @Override
                    public void onSuccess() {
                        if (mLoadingIndicator != null) {
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private class ImageLoadedCallback implements Callback {

        ProgressBar mLoadingIndicator;

        ImageLoadedCallback(ProgressBar loadingIndicator) {
            mLoadingIndicator = loadingIndicator;
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onError() {

        }
    }

    private void setAlsoKnownAs(List<String> alsoKnownAsList) {
        LinearLayout alsoKnownLayout = findViewById(R.id.also_known_layout);
        TextView alsoKnownTextView = findViewById(R.id.also_known_tv);

        if (alsoKnownAsList.isEmpty()) {
            alsoKnownLayout.setVisibility(View.GONE);
            return;
        }

        alsoKnownLayout.setVisibility(View.VISIBLE);
        alsoKnownTextView.setText(joinString(alsoKnownAsList));
    }

    private void setPlaceOfOrigin(String placeOfOrigin) {
        LinearLayout originLayout = findViewById(R.id.origin_layout);
        TextView placeOfOriginTextView = findViewById(R.id.origin_tv);

        if (TextUtils.isEmpty(placeOfOrigin)) {
            originLayout.setVisibility(View.GONE);
            return;
        }

        originLayout.setVisibility(View.VISIBLE);
        placeOfOriginTextView.setText(placeOfOrigin);
    }

    private void setIngredients(List<String> ingredients) {
        LinearLayout ingredientsLayout = findViewById(R.id.ingredients_layout);
        TextView ingredientsTextView = findViewById(R.id.ingredients_tv);

        if (ingredients.isEmpty()) {
            ingredientsLayout.setVisibility(View.GONE);
            return;
        }

        ingredientsLayout.setVisibility(View.VISIBLE);
        ingredientsTextView.setText(joinString(ingredients));
    }

    private void setDescription(String description) {
        LinearLayout descriptionLayout = findViewById(R.id.description_layout);
        TextView descriptionTextView = findViewById(R.id.description_tv);

        if (TextUtils.isEmpty(description)) {
            descriptionLayout.setVisibility(View.GONE);
            return;
        }

        descriptionLayout.setVisibility(View.VISIBLE);
        descriptionTextView.setText(description);
    }

    private String joinString(List<String> stringList) {
        String joinedString = "";
        for (String string : stringList) {
            joinedString = joinedString.concat(string + ", ");
        }
        joinedString = joinedString.substring(0, joinedString.length() - 2);
        return joinedString;
    }
}
