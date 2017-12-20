angular.module('UtilityServiceModule', []).factory('Utility', function() {
    var getType = function(o) {
        var _t;
        return ((_t = typeof(o)) == "object" ? o == null && "null" || Object.prototype.toString.call(o).slice(8, -1) : _t).toLowerCase();
    }

    var extend = function(destination, source) {
        for (var p in source) {
            if (getType(source[p]) == "array" || getType(source[p]) == "object") {
                destination[p] = getType(source[p]) == "array" ? [] : {};
                arguments.callee(destination[p], source[p]);
            } else {
                destination[p] = source[p];
            }
        }
    }

    //used for sorting in grid.
    var sortingRule = function(a, b) {
        a = Number(a);
        b = Number(b);
        if (a === b) {
            return 0;
        }
        if (a > b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
    };

    var combinePayments = function(ps) {
        var cs = [];
        ps.forEach(function(p) {
            var ele = cs.filter(function(v) {
                return v.CONTRACTID == p.CONTRACTID;
            });
            if (ele.length) {
                var c = ele[0];
                c.CUREAMT = c.CUREAMT + p.CUREAMT;
                c.RECEIPTAMT = c.RECEIPTAMT + p.RECEIPTAMT;
                c.RECEIPTASDAMT = c.RECEIPTASDAMT + p.RECEIPTASDAMT;
                c.INTAMT = c.INTAMT + p.INTAMT;
                c.PRINAMT = c.PRINAMT + p.PRINAMT;
                c.PENALTY = c.PENALTY + p.PENALTY;
                c.RENTAL = c.RENTAL + p.RENTAL;
                c.UNPAIDAMT = c.UNPAIDAMT + p.UNPAIDAMT;
                c.UNPAIDRENTAL = c.UNPAIDRENTAL + p.UNPAIDRENTAL;
                c.UNPAIDPENALTY = c.UNPAIDPENALTY + p.UNPAIDPENALTY;
            } else {
                cs.push({
                    CONTRACTID: p.CONTRACTID,
                    CONTRACTNUMBER: p.CONTRACTNUMBER,
                    DEALER: p.DEALER,
                    CUSTOMER: p.CUSTOMER,
                    CUREAMT: p.CUREAMT,
                    INTAMT: p.INTAMT,
                    PRINAMT: p.PRINAMT,
                    PENALTY: p.PENALTY,
                    RENTAL: p.RENTAL,
                    STATUS: p.STATUS,
                    RECEIPTAMT: p.RECEIPTAMT,
                    RECEIPTASDAMT: p.RECEIPTASDAMT,
                    UNPAIDAMT: p.UNPAIDAMT,
                    UNPAIDRENTAL: p.UNPAIDRENTAL,
                    UNPAIDPENALTY: p.UNPAIDPENALTY
                });
            };
        });
        return cs;
    };

    return {
        extend: function(destination, source) {
            return extend(destination, source);
        },
        sortingRule: function(a, b) {
            return sortingRule(a, b);
        },
        combinePayments:function(ps){
            return combinePayments(ps);
        }
    }
})
