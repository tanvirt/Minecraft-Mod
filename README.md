# Minecraft Mod

### Development Workflow

I. Sync with dev branch.

```sh
$ git fetch
$ git checkout dev
$ git pull origin dev
```

II. Create a new branch.

```sh
$ git checkout -b my-small-new-feature
```

III. Make changes on your new branch. Changes should come with tests.

```sh
$ git commit -a -m "A small commit"
$ git commit -a -m "Another small commit"
$ git commit -a -m "Yet another small commit"
```

IV. Re-sync with dev branch. A new commit is only neccessary if you are not already up-to-date.

```sh
$ git pull origin dev
$ git commit -a -m "A small merge with dev"
```

V. Run "All in Minecraft-Mod_test" with Coverage. Make sure all tests pass. If there are issues, repeat steps 3-4.

VI. Run "Minecraft Client". Make sure all changes are reflected properly and app doesn't crash. If there are issues, repeat steps 3-5.


VII. Push changes upstream.
```sh
$ git push origin my-small-new-feature
```

VIII. Create pull request for the [repo][git-repo-url] with "base: dev" and "compare: my-small-new-feature".

IX. Repeat the above steps for continued development.

[//]: # (These reference links get stripped out and shouldn't be seen.)

   [git-repo-url]: <https://github.com/tanvirt/Minecraft-Mod>
