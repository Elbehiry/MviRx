dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "DinDinn"
include(
    ":app",
    ":depconstraints",
    ":shared",
    ":model",
    ":test_shared"
)