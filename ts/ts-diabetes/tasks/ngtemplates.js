/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by stephenwhite on 27/03/15.
 */
module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/webcert',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/ts-diabetes/webcert/templates.js',
        options:{
            module: 'ts-diabetes',
            url: function(url) {
                return '/web/webjars/ts-diabetes/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/minaintyg',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/ts-diabetes/minaintyg/templates.js',
        options:{
            module: 'ts-diabetes',
            url: function(url) {
                return '/web/webjars/ts-diabetes/minaintyg/' + url;
            }
        }
    }
};

