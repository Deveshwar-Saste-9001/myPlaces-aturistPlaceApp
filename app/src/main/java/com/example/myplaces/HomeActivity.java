package com.example.myplaces;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplaces.DatabaseFiles.DatabaseCodes;
import com.example.myplaces.Fragments.HomeFragment;
import com.example.myplaces.Fragments.SchoolFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView school;
    private FrameLayout frameLayout;
    private static final int HOME_FRAGMENT = 0;
    private NavigationView navigationView;
    private static final int SCHOOLS_FRAGMENT = 1;
    private static final int ATTENDANCE_FRAGMENT = 2;
    private FirebaseUser CurrentUser;
    private static final int CONTACT_US = 8;
    private int currentFragment = -1;
    private CircleImageView profileView;
    private TextView FullName, email,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);
        gotoFragment("Trip Treasure", new HomeFragment(), HOME_FRAGMENT);

        FullName = navigationView.getHeaderView(0).findViewById(R.id.userNameprofile);
        email = navigationView.getHeaderView(0).findViewById(R.id.userEmailprofile);
       // location=navigationView.getHeaderView(0).findViewById(R.id.userLocationprofile);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DatabaseCodes.email = task.getResult().getString("Email");
                    DatabaseCodes.username = task.getResult().getString("Name");
                    DatabaseCodes.location = task.getResult().getString("Location");

                    FullName.setText(DatabaseCodes.username);
                    email.setText(DatabaseCodes.email);
                   // location.setText(DatabaseCodes.location);


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();

                }
            }
        });


}





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Paper.book().delete("user");
            Intent mainmenuIntent=new Intent(HomeActivity.this,MainMenuActivity.class);
            startActivity(mainmenuIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            gotoFragment("OMOTEC", new HomeFragment(), HOME_FRAGMENT);
        } else if (id == R.id.nav_Attendance) {

        } else if (id == R.id.nav_DailyLogs) {
//4
        }  else if (id == R.id.nav_AboutUs) {
//7
        } else if (id == R.id.nav_ContactUs) {

        } else if (id == R.id.nav_logOut) {
            FirebaseAuth.getInstance().signOut();
            Paper.book().delete("user");
            Intent mainmenuIntent=new Intent(HomeActivity.this,MainMenuActivity.class);
            startActivity(mainmenuIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoFragment(String Title, Fragment fragment, int FragmentNo) {
        invalidateOptionsMenu();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(Title);
        setFragment(fragment, FragmentNo);
    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.commit();
        }
    }
}
