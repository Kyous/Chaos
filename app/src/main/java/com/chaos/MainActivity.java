package com.chaos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Data;
import com.chaos.ui.About;
import com.chaos.ui.Categories;
import com.chaos.ui.Home;
import com.chaos.ui.LeftNavAdapter;
import com.chaos.ui.Settings;
import com.chaos.ui.VideoList;
import com.chaos.utils.ExceptionHandler;

import java.util.ArrayList;

/**
 * The Class MainActivity is the base activity class of the application. This
 * activity is launched after the Splash and it holds all the Fragments used in
 * the app. It also creates the Navigation Drawer on left side.
 */
public class MainActivity extends CustomActivity {

    /**
     * The drawer layout.
     */
    private DrawerLayout drawerLayout;

    /**
     * ExpandableListView for left side drawer.
     */
    private ExpandableListView drawerLeft;

    /**
     * The drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * The left navigation list adapter.
     */
    private LeftNavAdapter adapter;

    /* (non-Javadoc)
     * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setupContainer();
        setupDrawer();
    }

    /**
     * Setup the drawer layout. This method also includes the method calls for
     * setting up the Left side drawer.
     */
    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                setActionBarTitle();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setActionBarTitle();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawers();

        setupLeftNavDrawer();
    }

    /**
     * Setup the left navigation drawer/slider. You can add your logic to load
     * the contents to be displayed on the left side drawer. You can also setup
     * the Header and Footer contents of left drawer if you need them.
     */
    private void setupLeftNavDrawer() {
        drawerLeft = (ExpandableListView) findViewById(R.id.left_drawer);

        adapter = new LeftNavAdapter(this, getDummyLeftNavItems());
        drawerLeft.setAdapter(adapter);

        drawerLeft.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                drawerLayout.closeDrawers();
                launchFragment(groupPosition, childPosition);
                return true;
            }
        });

        drawerLeft.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (adapter.getChildrenCount(groupPosition) == 0) {
                    drawerLayout.closeDrawers();
                    launchFragment(groupPosition, 0);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * This method returns a list of dummy items for left navigation slider. You
     * can write or replace this method with the actual implementation for list
     * items.
     *
     * @return the dummy items
     */
    private ArrayList<Data> getDummyLeftNavItems() {
        ArrayList<Data> al = new ArrayList<Data>();
        al.add(new Data("Home", R.drawable.icon_home, null));
        al.add(new Data("Categories", 0, new String[]{"Art and Culture",
                "Comedy", "Drama", "Lifestyle", "News", "Movie", "Sport",
                "Trailers"}));
        al.add(new Data("Watchlist", 0, null));
        al.add(new Data("History", 0, null));
        al.add(new Data("Watch Later", 0, null));
        al.add(new Data("Settings", 0, null));
        al.add(new Data("About", 0, null));
        return al;
    }

    /**
     * This method can be used to attach Fragment on activity view for a
     * particular left navigation slider item position. You can customize this method as per your need.
     *
     * @param pos   the group position of left navigation slider item selected.
     * @param chPos the child position
     */
    public void launchFragment(int pos, int chPos) {
        Fragment f = null;
        String title = null;
        if (pos == 0) {
            title = "";
            f = new Home();
            f.setArguments(null);
        } else if (pos == 1) {
            title = adapter.getChild(pos, chPos);
            f = new Categories();
        } else if (pos == 2 || pos == 3 || pos == 4) {
            title = adapter.getGroup(pos).getTitle1();
            f = new VideoList();
        } else if (pos == 5) {
            title = "Settings";
            f = new Settings();
        } else if (pos == 6) {
            title = "About";
            f = new About();
        }

        if (f != null) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, f).addToBackStack(title)
                    .commit();
        }
    }

    /**
     * Setup the container fragment for drawer layout. The current
     * implementation of this method simply calls launchFragment method for
     * position 0. You can customize this method as per your need to display
     * specific content.
     */
    private void setupContainer() {
        getSupportFragmentManager().addOnBackStackChangedListener(
                new OnBackStackChangedListener() {

                    @Override
                    public void onBackStackChanged() {
                        setActionBarTitle();
                    }
                });
        launchFragment(0, 0);
    }

    /**
     * Set the action bar title text.
     */
    private void setActionBarTitle() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(drawerLeft)) {
                getActionBar().setTitle("");
                getActionBar().setLogo(R.drawable.icon);
                return;
            }
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            return;
        String title = getSupportFragmentManager().getBackStackEntryAt(
                getSupportFragmentManager().getBackStackEntryCount() - 1)
                .getName();
        getActionBar().setTitle(title);
        getActionBar().setLogo(
                title.length() == 0 ? R.drawable.icon : R.drawable.trans1);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

	/*@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}*/

    /* (non-Javadoc)
     * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            } else
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* (non-Javadoc)
     * @see com.events.custom.CustomActivity#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

}
