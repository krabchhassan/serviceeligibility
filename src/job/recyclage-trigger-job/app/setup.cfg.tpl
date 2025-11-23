{{#app}}
[metadata]
name = {{name}}
description =
long_description = file: README.md
long_description_content_type = text/markdown
author =
classifiers =
    Operating System :: OS Independent
    Programming Language :: Python :: 3.7
python_requires = "=={{pythonVersion}}"
version = {{version}}
{{/app}}

[options]
package_dir =
    =src
packages = find_namespace:

install_requires =
    {{#libs}}
    {{libName}} == {{libVersion}}
    {{/libs}}
    beyondpythonframework==16.0.14
    beyondpythonframework==16.0.14
    commonpersonalworkdir==16.0.2

[options.package_data]
* =  *.json, *.conf

[options.packages.find]
where = src

[options.entry_points]
console_scripts =
    recyclagetriggerjob=recyclagetriggerjob:main
