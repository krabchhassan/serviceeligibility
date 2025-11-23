{{#app}}
[metadata]
name = {{name}}
description =
long_description = file: README.md
long_description_content_type = text/markdown
author =
classifiers =
    Operating System :: OS Independent
    Programming Language :: Python :: 3.11
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
    cachetools==5.3.1
    aiohttp==3.8.6
    dataclasses-json==0.5.6
    faashelper==16.0.15
    organisationmlssettings==16.2.17

[options.package_data]
* =  *.json, *.conf

[options.packages.find]
where = src
