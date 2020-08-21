//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
// 

#include "SinkStat.h"

    Define_Module(SinkStat);

void SinkStat::initialize()
{
    Sink::initialize();
    customer = 0;
}

void SinkStat::handleMessage(cMessage *msg)
{
    Sink::handleMessage(msg);
    customer += 1;
}

void SinkStat::finish()
{
    cModule* ptr =
    this->getParentModule()->getSubmodule("resourcePool");
    cModule* ptr1 =
    this->getParentModule()->getSubmodule("resourcePool1");
    cModule* ptr2 =
    this->getParentModule()->getSubmodule("resourcePool2");
    int num1 = ptr->par("amount");
    int num2 = ptr1->par("amount");
    int num3 = ptr2->par("amount");
    std::cout << "Registration=" << num1 << ", Picking=" << num2 <<  ", Delivery=" << num3;
    std::cout <<", Customers=" << customer << endl;
    Sink::finish();
}
