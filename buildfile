require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/jacoco'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

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
  pom.include_transitive_dependencies << artifact(:javax_annotation)
  pom.include_transitive_dependencies << artifact(:jetbrains_annotations)
  pom.include_transitive_dependencies << artifact(:javacsv)
  pom.include_transitive_dependencies << artifact(:testng)
  pom.dependency_filter = Proc.new {|dep| dep[:scope].to_s != 'test'}

  compile.with :javax_annotation,
               :jetbrains_annotations,
               :javacsv,
               :testng

  test.options[:java_args] = ['-ea']

  test.using :testng
  test.compile.with :guiceyloops

  package(:jar)
  package(:sources)
  package(:javadoc)

  iml.excluded_directories << project._('tmp')

  ipr.add_default_testng_configuration(:jvm_args => '-ea')
  ipr.add_component_from_artifact(:idea_codestyle)
end
