require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/jacoco'

PROVIDED_DEPS = [:javax_jsr305, :jetbrains_annotations]
TEST_DEPS = [:guiceyloops]

desc 'GWT SymbolMap Assertions Library'
define 'gwt-symbolmap' do
  project.group = 'org.realityforge.gwt.symbolmap'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/gwt-symbolmap')
  pom.add_developer('realityforge', 'Peter Donald')

  pom.provided_dependencies.concat PROVIDED_DEPS
  compile.with PROVIDED_DEPS,
               :javacsv,
               :testng

  test.options[:java_args] = ['-ea']

  test.using :testng
  test.compile.with TEST_DEPS

  package(:jar)
  package(:sources)
  package(:javadoc)

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea')
  ipr.add_component_from_artifact(:idea_codestyle)
end
